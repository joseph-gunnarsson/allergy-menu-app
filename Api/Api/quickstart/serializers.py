from .models import *
from rest_framework import serializers


class userS(serializers.HyperlinkedModelSerializer):
    class Meta:
     model = user
     fields = ['first_name', 'last_name', 'password',"email","id"]
class restaSER(serializers.HyperlinkedModelSerializer):
    class Meta:
     model = restaurant
     fields = ['restaurant_name', 'address']
class AllergyGroupSER(serializers.HyperlinkedModelSerializer):
   class Meta:
     model = AllergyGroups
     fields = ['AllergyGroups','id']
class IngredientsSER(serializers.HyperlinkedModelSerializer):
   class Meta:
     model=Ingredients
     fields = ['Ingredient']
class AllergyIListSER(serializers.HyperlinkedModelSerializer):
   class Meta:
    model=AllergyIngredientsList
    fields = ['User_id',"Ingredient_id"]
class AllergyGroupsLSER(serializers.HyperlinkedModelSerializer):
   class Meta:
    model=AllergyGroupList
    fields = ['User_id',"AllergyGroups_id"]
class MenuItemSER(serializers.HyperlinkedModelSerializer):
   class Meta:
    model=menuItem
    fields = ['restaurant_id',"Section","menuItem_name","id"]
class menuAllergyGroupsSER(serializers.HyperlinkedModelSerializer):
   class Meta:
    model=menuAllergyGroups
    fields = ['menuItem_id',"menuItem_AllergyGroups"]
class menuIngredientsSER(serializers.HyperlinkedModelSerializer):
   class Meta:
    model=menuIngredients
    fields = ['menuItem_id',"menuItem_ingredient"]        




    



