from django.contrib import admin
from .models import *


admin.site.register(user)
admin.site.register(Ingredients)
admin.site.register(AllergyGroups)
admin.site.register(AllergyIngredientsList)
admin.site.register(restaurant)
admin.site.register(menuItem)
admin.site.register(menuAllergyGroups)
admin.site.register(menuIngredients)
admin.site.register(AllergyGroupList)




