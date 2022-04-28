from Api.quickstart.models import *
from rest_framework import viewsets
from .serializers import *
from django.http import HttpResponse
from django.http import JsonResponse
from django.core import serializers
from rest_framework.renderers import JSONRenderer
from rest_framework.response import Response
from rest_framework.decorators import api_view, renderer_classes
import json
from django.core.serializers import serialize
from django.forms.models import model_to_dict
from django.core.exceptions import ObjectDoesNotExist
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django import template
from string import ascii_uppercase
from django.shortcuts import redirect
import hashlib
import random
import string
import re

"""Signs up user for allergy menu app"""
@api_view(('GET','POST',))
def signup(request):
                #for testing        
                if request.method == 'GET':    
                    x=user.objects.all()
                    serializer_class = userS(x,many=True) 
                #checks if request type is post    
                if request.method == 'POST':
                    #gets the user data from the request  
                    first_name=request.data.get("first_name")
                    email=request.data.get("email")
                    password=request.data.get("password")
                    #creates json object
                    response_data = {}
                    #checks if user already exists
                    if user.objects.filter(email = email).count()>0:
                        #returns error message if so
                        response_data["email"] = "email already exit"
                        return Response(response_data)
                    #sets message to sucsses so app knows user was added
                    response_data["email"]="Sucsses"
                    #creates a new user and adds it to user table
                    user.objects.create(fullname=first_name,email=email,password=hashlib.sha256(password.encode('utf-8')).hexdigest()).save() 
                    #sends response data
                    return Response(response_data)
"""Logs in user"""
@api_view(('POST',))
def login(request):     
                #checks request type   
                if request.method == 'POST':  
                    #try which catches if user does not exist
                    try:
                     #gets the logging in user infomration from request
                     email=request.data.get("email")
                     password=request.data.get("password")  
                     token=request.data.get("token")
                     #gets the the users account infomration from email recived
                     useraccount=user.objects.get(email = email)
                     #creates new json object
                     response_data = {}  
                     response_data['message']="fail"
                     #checks if password recived matches password in       
                     if(useraccount.password==hashlib.sha256(password.encode('utf-8')).hexdigest()):
                        #Sets the token 
                        user.objects.filter(email = email).update(token=hashlib.sha256(token.encode('utf-8')).hexdigest())
                        #response back with the users information
                        response_data['message'] = 'Sucsses'
                        response_data['name'] =  useraccount.fullname
                        response_data['id'] =  useraccount.id
                        response_data['email'] =  useraccount.email
                        return Response(response_data)
                     else:
                        #if password doesnt exists response with error message
                        return Response(response_data)
                    except user.DoesNotExist:
                     response_data = {}  
                     response_data['message']="fail"
                     return Response(response_data)
"""Second method to login by checking if token on devices matches token any token in table"""
@api_view(('POST',))
def checktoken(request):
    if request.method=="POST":
        #creates json object and gets the token of the request
        response_data = {}
        token=request.data.get("token")
        #checks if token exists in user table
        if user.objects.filter(token = hashlib.sha256(token.encode('utf-8')).hexdigest()).exists():
            #if so responses back with users information
            useraccount=user.objects.get(token = hashlib.sha256(token.encode('utf-8')).hexdigest())
            response_data['message'] = 'Sucsses'    
            response_data['name'] =  useraccount.fullname
            response_data['id'] =  useraccount.id
            response_data['email'] =  useraccount.email
            return Response(response_data)
        #else user device not got an account linked with it
        response_data["message"]="no"
        return Response(response_data)



"""updates allegy information for user"""
@api_view(('POST','GET'))
def UpdateAllergyList(request):
    if request.method=="POST":
        #creates json objec
        response_data = {}
        #gets the post data
        user_id=request.data.get("User_id")
        ingredients=request.data.get("AllergyList")
        table=request.data.get("Table")
        #sets the table that is being updated
        if(table=="AllergyIngredientsList"):
            table=AllergyIngredientsList
        else:
            table=AllergyGroupList
        #set response message to show data has been updated
        response_data['message'] = 'Sucsses'
        response_data["Ingredient"]=ingredients
        #gets the user of the request
        user1=user.objects.get(id=user_id)
        #checks if user has currently any allergy info set
        if(table.objects.filter(User_id=user_id).exists()):
            #if so deletes all allergy info
            table.objects.filter(User_id=user_id).delete()
        #for loop that adds all of the allergy information in the request data
        for ingredient in ingredients:     
            if(request.data.get("Table")=="AllergyGroupList"):
                ingredient_id=AllergyGroups.objects.get(AllergyGroups=ingredient)
                table.objects.create(AllergyGroups_id=ingredient_id,User_id=user1).save()
            else:
                #checks if ingredient exists
                if(not Ingredients.objects.filter(Ingredient=ingredient).exists()):
                    #if not adds a new ingredient
                    Ingredients.objects.create(Ingredient=ingredient).save()         
                ingredient_id=Ingredients.objects.get(Ingredient=ingredient)
                table.objects.create(Ingredient_id=ingredient_id,User_id=user1).save()
        return Response(response_data)  
"""Reuturns a list of all ingredients in database"""
@api_view(('POST','GET'))
def getIngredients(request):
    if request.method=="GET":
        a=AllergyIngredientsList.objects.get(User_id=8)
        print(a.Ingredient_id.Ingredient)
        response_data={}
        response_data["ingredients"]=[]
        for ing in Ingredients.objects.all():
            response_data["ingredients"].append(ing.Ingredient)
        return Response(response_data)
"""Gets the menu of a resturant"""
@api_view(('POST','GET'))
def getMenu(request):
    if request.method=="POST":
        #gets the resturant id from post data
        restid=request.data.get("restid")
        #creates new json object
        response_data={}
        #checks resutrant exists
        if(not(restaurant.objects.filter(id=restid).exists()))   :
            response_data['message']="restaurant does not exist"
            return Response(response_data)
        #gets the resturant infomation from table
        restaurantpro=restaurant.objects.get(id=restid)
        #add the resturant name to object
        response_data['RestName']=restaurantpro.restaurant_name
        #gets all menu items for restaurant
        menuitemsz=menuItem.objects.filter(restaurant_id=restaurantpro)
        #add menu items to response data
        response_data['MenuItems']=[]
        for m in menuitemsz:
            item={}
            item['Name']=m.menuItem_name
            item['Section']=m.Section
            item['id']=m.id
            item['price']=m.price
            item['ingredients']=[]
            for ingredients in menuIngredients.objects.filter(menuItem_id=m):
                item['ingredients'].append(ingredients.menuItem_ingredient.Ingredient)
            item['allergygroups']=[]   
            for ingredients in menuAllergyGroups.objects.filter(menuItem_id=m):
                item['allergygroups'].append(ingredients.menuItem_AllergyGroups.AllergyGroups)
            response_data['MenuItems'].append(item)
        return Response(response_data)
"""checks if resturant exists"""
@api_view(('POST','GET'))
def checkrest(request):
    if request.method=="POST":
        #gets the id of resturant to check
        id=request.data.get("id")
        response_data={}
        #if resturant exists repsonse with true
        if restaurant.objects.filter(id=id).exists():
            response_data["message"]="true"
            return Response(response_data)
        #else responses with error message
        response_data["message"]="invalid qr"
        return Response(response_data)
"""adds new order"""
@api_view(('POST','GET'))
def order(request):
    if request.method=="POST":
        #gets the post data 
        id=request.data.get("id")
        items=request.data.get("items")
        restid=request.data.get("restid")
        #sets the resturant for the order
        rest=restaurant.objects.get(id=restid)
        #creates json object
        response_data={}
        #adds each order item to orders data base
        for i in items:
            orders.objects.create(restaurant_id=rest,order=i).save()
        response_data["message"]="true"
        #response to show order added
        return Response(response_data)
"""Gets all the address and name of each resturant"""
@api_view(('POST','GET'))
def getAddresses(request):
     if request.method=="POST":
       #gest all restaruants in database 
       x=restaurant.objects.all()
       #gets all restaurant names and addresses
       serializer_class = restaSER(x,many=True)
       #creates json object
       response_data={}
       #adds resturants names and addresses to response data
       response_data["info"]=serializer_class.data
       return Response(response_data) 
"""logs user out allergy app"""
@api_view(('POST','GET'))
def logouta(request):
     if request.method=="POST":
        #gets id of logging out user
        id=request.data.get("id")
        #removes the token from user to show user if no longer logged in
        user.objects.filter(id=id).update(token="null")
        response_data={}
        response_data["message"]="true"
        return Response(response_data)


"""Gets the allergy information of the user sending request"""
@api_view(('POST','GET'))
def getAllergies(request):
    if request.method=="POST":
        #gets the users id from post data
        user_id=request.data.get("user_id")
        #intialses json response data
        response_data={}
        #if user doesnt not exist responses with error message
        if(not user.objects.filter(id=user_id).exists()):
            response_data['message']="User DoesNotExist"
            return Response(response_data)
        #querys the users table for the user id
        useraccount=user.objects.get(id=user_id)
        #creates a jsonArray in the json object
        response_data['Allergylist']=[]
        #Try to catch if user has no allergy ingredients in set
        try:
          #fills Allergy list with all ingredients for useraccount
         for ing in AllergyIngredientsList.objects.filter(User_id=useraccount):
             response_data['Allergylist'].append(ing.Ingredient_id.Ingredient)
        except AllergyIngredientsList.DoesNotExist:
             print("true")
        #Same as above but fot allergy groups
        
        response_data['AllergyGroup']=[]
        try:
         for ing in AllergyGroupList.objects.filter(User_id=useraccount):
             response_data['AllergyGroup'].append(ing.AllergyGroups_id.AllergyGroups)
        except AllergyGroupList.DoesNotExist:
             print("true")
        #returns the response_data
        return Response(response_data)
"""Adds a menu item to resturants menu"""
@api_view(('POST','GET'))
def addmenuItem(request):
    if request.method=="POST":
        #gets all of the post data
        a=request.data
        response_data={}
        menuitemName=a.get("name")
        section=a.get("section")
        price=a.get("price")
        ingredients=a.get("ingredients")
        allergygroups=a.get("allergygroups")
        #validates the resturant
        restid=restaurant.objects.get(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
        #checks if menu item allergy exists and response with error message
        if(menuItem.objects.filter(Section=section,menuItem_name=menuitemName).exists()):
            response_data["message"]="menu item already exists"
            return Response((response_data))
        #creates a new menu item
        menuItem.objects.create(restaurant_id=restid,Section=section,menuItem_name=menuitemName,price=price).save()
        #gets the newly create menu item
        menuitem=menuItem.objects.get(restaurant_id=restid,Section=section,menuItem_name=menuitemName)
        #fills the Ingredients tables with the ingredients in request data
        for ingredient in ingredients:
            if(not(Ingredients.objects.filter(Ingredient=ingredient).exists())):
                Ingredients.objects.create(Ingredient=ingredient).save()
            menuIngredients.objects.create(menuItem_ingredient=Ingredients.objects.get(Ingredient=ingredient),menuItem_id=menuitem).save()
        #fills the Allergygroups tables with the allergygroups in request data
        for group in allergygroups:
            menuAllergyGroups.objects.create(menuItem_AllergyGroups=AllergyGroups.objects.get(AllergyGroups=group),menuItem_id=menuitem).save()
        response_data["message"]="Sucsses"
        return Response((response_data))
    return render(request,'rest/home.html')
  

"""creates the edit menu item page"""
@api_view(('POST','GET'))
def editmenu(request):
    #gets the logged in resturant information
    rest=restaurant.objects.get(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
    #gets all the menu itesm for that resturant
    menu=menuItem.objects.filter(restaurant_id=rest)
    #creates a json object
    response_data={}
    #creates json array to hold menuitems
    response_data["items"]=[]
    for m in menu:
        #fill items with each menuitem
        item={}
        item["name"]=m.menuItem_name
        item["section"]=m.Section
        item["id"]=m.id
        item["price"]=m.price
        response_data["items"].append(item)
    if request.method=="POST":
        #post request to delete a specfic menu item 
        a=request.data.get("id")
        #deletes menu item from table
        menuItem.objects.filter(id=a).delete()
        #rerenders the menu page with the updated menu items
        return render(request,'rest/menu.html',{"created": True, "items": response_data["items"]})
    #renders menu page with the menu items
    return render(request,'rest/menu.html',{"created": True, "items": response_data["items"]})
"""renders the view orders page"""
@api_view(('POST','GET'))
def vieworders(request):
    #gets the logged in resturant information
    rest=restaurant.objects.get(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
    #gets all of the orders for that resturant
    order=orders.objects.filter(restaurant_id=rest)
    #creates json object
    response_data={}
    response_data["items"]=[]
    #fills the orders object
    for m in order:
        item={}
        item["order"]=m.order
        item["id"]=m.id
        response_data["items"].append(item)
    if request.method=="POST":
        #post to deal with the completion of order
        response_data={}
        #gets the id of order
        a=request.data.get("id")
        #deletes the order from database
        orders.objects.filter(id=a).delete()
        response_data["items"]=[]
        #gets the resturant orders
        menu=orders.objects.filter(restaurant_id=rest)
        response_data["items"]=[]
        #fills the orders object
        for m in menu:
           item={}
           item["order"]=m.order
           item["id"]=m.id
           response_data["items"].append(item)
        #rerenders the order page with the updated order list
        return render(request,'rest/order.html',{"created": True, "items": response_data["items"]})
    #renders the order page with the order list
    return render(request,'rest/order.html',{"created": True, "items": response_data["items"]})

"""updates a menu item"""
@api_view(('POST','GET'))
def updateitem(request):
    if request.method=="POST":
        #gets the post data
        a=request.data
        response_data={}
        menuitemName=a.get("name")
        section=a.get("section")
        price=a.get("price")
        id=a.get("id")
        #deletes the menu item from the database 
        menuItem.objects.filter(id=id).delete()
        ingredients=a.get("ingredients")
        allergygroups=a.get("allergygroups")
        #gests the logged in resturant
        restid=restaurant.objects.get(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
        #checks menu item already exists
        if(menuItem.objects.filter(Section=section,menuItem_name=menuitemName).exists()):
            response_data["message"]="menu item already exists"
            return Response((response_data))
        #adds the menu item
        menuItem.objects.create(restaurant_id=restid,Section=section,menuItem_name=menuitemName,price=price).save()
        menuitem=menuItem.objects.get(restaurant_id=restid,Section=section,menuItem_name=menuitemName)
        print(menuitem)
        for ingredient in ingredients:
            if(not(Ingredients.objects.filter(Ingredient=ingredient).exists())):
                Ingredients.objects.create(Ingredient=ingredient).save()
            menuIngredients.objects.create(menuItem_ingredient=Ingredients.objects.get(Ingredient=ingredient),menuItem_id=menuitem).save()
        for group in allergygroups:
            menuAllergyGroups.objects.create(menuItem_AllergyGroups=AllergyGroups.objects.get(AllergyGroups=group),menuItem_id=menuitem).save()
        response_data["message"]="Sucsses"
        return Response((response_data))
    return render(request,'rest/home.html')




"""deals with the login and sign of resturant web page"""
def restSignUp(request):
    #if the request has session key creates one
    if not request.session.exists(request.session.session_key):
        request.session.create()
    if request.method=="POST":
        #gets the post data
        email=request.POST["email"]
        password=request.POST.get("passwords") 
        #checks if resturant for email exists
        if(not(restaurant.objects.filter(email=email).exists())):
            #if so render signup with invalid set to true displaying arror mesage
            return render(request,'rest/signup.html',{"invalid": True ,"created": False ,"error": False })
        #gets resturant for email
        rest=restaurant.objects.get(email=email)
        #if so render signup with invalid set to true displaying arror mesage
        if(not(password==rest.restaurant_password)):
            #if not render sign up with inb
            return render(request,'rest/signup.html',{"invalid": True ,"created": False ,"error": False })          
        #if password matches sets the token to the hash of session id
        restaurant.objects.filter(email=email).update(IsAuth=True,token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
        #gets restaurant menu information
        rest=restaurant.objects.get(email=email)
        response_data={}
        #gets the menu items for the resturant
        response_data=getmenudate(rest)
        #renders menu with the menu items
        return render(request,'rest/menu.html',{"items": response_data["items"]})
    #if a resturant contains the current sesssion id auto log them in
    if(restaurant.objects.filter(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest()).exists()):
        rest=restaurant.objects.get(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
        response_data={}
        response_data=getmenudate(rest)
        return render(request,'rest/menu.html',{"items": response_data["items"]})    
    #renders the signup page
    return render(request,'rest/signup.html',{"invalid": False ,"created": False ,"error": False })  

"""Renders the update page of a menu item"""
def update(request):
    if request.method=="POST":
        #gets id of menu item
        id=request.POST["id"]
        menu=menuItem.objects.get(id=id)
        print(menu)
        ing=menuIngredients.objects.filter(menuItem_id=menu)
        ag=menuAllergyGroups.objects.filter(menuItem_id=menu)
        response_data={}
        response_data["ing"]=[]
        response_data["ag"]=[]
        #set json array with ingredients and allergygroups of menuitem
        for i in ing:
            response_data["ing"].append(i.menuItem_ingredient.Ingredient)
        for i in ag:
            response_data["ag"].append(i.menuItem_AllergyGroups.AllergyGroups)
        response_data["section"]=menu.Section
        response_data["name"]=menu.menuItem_name
        response_data["price"]=menu.price
        response_data["id"]=request.POST["id"]
        #renders update with the menu items infomration in response data
        return render(request,'rest/update.html',response_data)






"""gets all menu items for resturant rest"""
def getmenudate(rest):
    menu=menuItem.objects.filter(restaurant_id=rest)
    response_data={}
    response_data["items"]=[]
    for m in menu:
          item={}
          item["name"]=m.menuItem_name
          item["price"]=m.price
          item["section"]=m.Section
          item["id"]=m.id
          response_data["items"].append(item)
    return response_data


def home(request):
    #renders add menu item page
    if(restaurant.objects.filter(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest()).exists()):
        return render(request,'rest/home.html',{"invalid": False ,"created": False ,"error": False })  
    return render(request,'rest/signup.html',{"invalid": False ,"created": False ,"error": False })  
def logout(request):
    #logs out user
    if request.method=="GET":
        restaurant.objects.filter(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest()).update(token=None,IsAuth=False)
        return redirect('restsignup/') 
def qr(request):
    #render qr page
    if(restaurant.objects.filter(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest()).exists()):
        r=restaurant.objects.get(token=hashlib.sha256(request.session.session_key.encode('utf-8')).hexdigest())
        print(r.id)
        return render(request,'rest/qr.html',{"id": r.id ,"created": False ,"error": False })  
    return render(request,'rest/signup.html',{"invalid": False ,"created": False ,"error": False })  
""" signs up user"""   
@api_view(('POST','GET'))
def SignupRest(request):
    if request.method=="POST":
        error=False
        message=""
        #gets post data of sign up information
        email=request.POST["createemail"]
        password=request.POST.get("createpassword")
        city=request.POST.get("city")
        postcode=request.POST.get("postcode")
        address=request.POST.get("address")+",",city+",",postcode
        restname=request.POST.get("name")
        #checks that all feilds had been inputed
        if(restname=="" or request.POST.get("address")=="" or city=="" or postcode==""):
            error=True
            message=message+"Please fill in all feilds"
            return render(request,'rest/signup.html',{"error": error,"message" : message,"created": False })
        #checks passwords of certain strength
        if(not(Password(str(password)))):  
            error=True
            message="Passwrod too weak,must be 8 characters long contain at least 1 number and uppercase,"
        #checks email exists or if email not invalid format
        if(not(Email(email)) or restaurant.objects.filter(email=email).exists()):
            error=True
            message=message+"Not a valid email or already in use,"
        #checks if any error with inputs occoured
        if(error):
            #renders signup with error messages
            return render(request,'rest/signup.html',{"error": error,"message" : message,"created": False })
        #if no errors creates the account
        restaurant.objects.create(email=email,restaurant_password=password,restaurant_name=restname,address=address).save()
    #renders signup with created message
    return render(request,'rest/signup.html',{"created": True, "error": False})


"""checks password strength"""
def Password(str):
    reg ="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    return re.match(reg,str)
"""checks valid email"""   
def Email(str):
    reg ="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    return re.match(reg,str)
    

























