# Generated by Django 3.0.4 on 2020-06-04 12:20

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('quickstart', '0008_auto_20200531_0542'),
    ]

    operations = [
        migrations.AddField(
            model_name='menuitem',
            name='price',
            field=models.CharField(default='0', max_length=50),
        ),
    ]