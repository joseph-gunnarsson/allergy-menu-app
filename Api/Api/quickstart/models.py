from django.db import models
# Create your models here.

class user(models.Model):
    fullname = models.CharField(max_length=60,default='null')
    password= models.CharField(max_length=64)
    email=models.CharField(max_length=30)
    token = models.CharField(max_length=64, null=True)

class restaurant(models.Model):
	email=models.CharField(max_length=30,default='null')
	address=models.CharField(max_length=60,default='null')
	restaurant_name=models.CharField(max_length=50)
	token = models.CharField(max_length=64, null=True)
	restaurant_password=models.CharField(max_length=64)
	IsAuth=models.BooleanField(default=False)
	def Auth(self):
		return IsAuth
class AllergyGroups(models.Model):
    AllergyGroups=models.CharField(max_length=50)
class Ingredients(models.Model):
    Ingredient=models.CharField(max_length=50)
class AllergyIngredientsList(models.Model):
	User_id=models.ForeignKey(user,on_delete=models.CASCADE)
	Ingredient_id=models.ForeignKey(Ingredients,on_delete=models.CASCADE)
class AllergyGroupList(models.Model):
	User_id=models.ForeignKey(user,on_delete=models.CASCADE)
	AllergyGroups_id=models.ForeignKey(AllergyGroups,on_delete=models.CASCADE)
class menuItem(models.Model):
	restaurant_id=models.ForeignKey(restaurant,on_delete=models.CASCADE)
	Section=models.CharField(max_length=50)
	price=models.CharField(max_length=50,default="0")
	menuItem_name=models.CharField(max_length=50)
class menuAllergyGroups(models.Model):
	menuItem_id=models.ForeignKey(menuItem,on_delete=models.CASCADE)
	menuItem_AllergyGroups=models.ForeignKey(AllergyGroups,on_delete=models.CASCADE)
class menuIngredients(models.Model):
	menuItem_id=models.ForeignKey(menuItem,on_delete=models.CASCADE)
	menuItem_ingredient=models.ForeignKey(Ingredients,on_delete=models.CASCADE)
class orders(models.Model):
    restaurant_id=models.ForeignKey(restaurant,on_delete=models.CASCADE)
    order=models.CharField(max_length=100)






    
