# Generated by Django 3.0.4 on 2020-03-25 21:30

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('quickstart', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='AllergyGroups',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('AllergyGroups', models.CharField(max_length=50)),
            ],
        ),
        migrations.CreateModel(
            name='Ingredients',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('Ingredient', models.CharField(max_length=50)),
            ],
        ),
        migrations.CreateModel(
            name='menuItem',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('Section', models.CharField(max_length=50)),
                ('menuItem_name', models.CharField(max_length=50)),
                ('restaurant_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.restaurant')),
            ],
        ),
        migrations.CreateModel(
            name='menuIngredients',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('menuItem_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.menuItem')),
                ('menuItem_ingredient', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.Ingredients')),
            ],
        ),
        migrations.CreateModel(
            name='menuAllergyGroups',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('menuItem_AllergyGroups', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.AllergyGroups')),
                ('menuItem_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.menuItem')),
            ],
        ),
        migrations.CreateModel(
            name='AllergyIngredientsList',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('Ingredient_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.Ingredients')),
                ('User_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.user')),
            ],
        ),
        migrations.CreateModel(
            name='AllergyGroupList',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('AllergyGroups_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.AllergyGroups')),
                ('User_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='quickstart.user')),
            ],
        ),
    ]
