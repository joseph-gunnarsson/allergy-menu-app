# Generated by Django 3.0.4 on 2020-05-31 05:40

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('quickstart', '0006_auto_20200529_0836'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='token',
            field=models.CharField(max_length=64, null=True),
        ),
    ]