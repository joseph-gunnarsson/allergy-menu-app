
var menuItem = new Object();

menuItem.ingredients=[]
menuItem.allergygroups=[]
count=0
var allergns=["Celery","Gluten","Crustaceans","Eggs","Fish","Peanuts","Sesame","Lupin","Milk","Molluscs","Mustard","Nuts","Soya","Sulphur dioxide"]

function adding(data){
    for (i = 0; i < data.length; i++) {
   $("#list").append("<li id='"+count+"'class='btn4'  >"+data[i]+"</li>");
      count++;
        $(function () {
    $('#list').on('click', 'li', function () {
           count--;
        $(this).remove();
    });
});
  }
}
function checkbox(data){
  for (i = 0; i < data.length; i++){
    $( "#"+data[i]).prop( "checked", true );
  }
}


$("#addIngredient").click(function(){
     Ing=$("#ingredient").val().toLowerCase();
     if(Ing!="" && !checkin(Ing)){
        
     	$("#list").append("<li id='"+count+"'class='btn4'  >"+Ing+"</li>");
     	count++;
     	$("#ingredient").val("");
   $(function () {
    $('#list').on('click', 'li', function () {
           count--;
        $(this).remove();
    });
});}});


for (var i = 0; i < allergns.length; i++) {
	$( ".allergnsbox" ).append( '<input type="checkbox" id="'+allergns[i]+'" name="'+allergns[i]+'" value="'+allergns[i]+'"><label for="vehicle1">'+allergns[i]+'</label><br>' );
}

$("#Add").click(function(){

$('ul li').each(function(i)
{
   menuItem.ingredients.push($(this).text())
});



   for(var i=0;i<allergns.length;i++){
  	console.log($('#'+allergns[i]+':checked').val())
  	if($('#'+allergns[i]+':checked').val()!=null)
  	menuItem.allergygroups.push($('#'+allergns[i]+':checked').val())
  }


   menuItem.id=$('#id').attr('name')
  menuItem.section=$('#section').val()
  menuItem.name=$('#name').val()
  menuItem.price=$('#price').val()
  if(menuItem.section=="" || menuItem.name==""){
    alert("Please complete all fields")
  }
  else{
  post("https://www.doc.gold.ac.uk/usr/391/updatefinal/",JSON.stringify(menuItem))
  window.location.href = "https://www.doc.gold.ac.uk/usr/391/editmenu/"}
  
});




function checkin(t){
	 for(var i=0;i<count;i++){
	 	if($("#"+i).text()==t)
	 		return true
 }
 return false
}

		  

