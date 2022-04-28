from django.urls import include, path,re_path
from rest_framework import routers
from Api.quickstart import views,models
from django.contrib import admin




# Wire up our API using automatic URL routing.
# Additionally, we include login URLs for the browsable API.
urlpatterns = [
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    path('admin/', admin.site.urls),
    path('signup/',views.signup),
    path('login/',views.login),
    path('logouta/',views.logouta),
    path('update/',views.UpdateAllergyList),
    path('GetIng/',views.getIngredients),
    path('GetAll/',views.getAllergies),
    path('getRest/',views.getMenu),
    path('tokencheck/',views.checktoken),
    path('checkrest/',views.checkrest),
    path('order/',views.order),
    path('address/',views.getAddresses),
    re_path(r'restsignup/',views.restSignUp),
    re_path(r'updateitem/',views.update),
    re_path(r'updatefinal/',views.updateitem),
    re_path(r'addm/',views.addmenuItem),
    re_path(r'h/',views.home,name="h"),
    re_path(r'vieworders/',views.vieworders,name="vieworders"),
    re_path(r'restcreate/',views.SignupRest),
    re_path(r'logout/',views.logout,name='logout'),
    re_path(r'editmenu/',views.editmenu,name='menu'),
    re_path(r'add/',views.editmenu,name='add'),
    re_path(r'qr/',views.qr,name='qr'),


]	