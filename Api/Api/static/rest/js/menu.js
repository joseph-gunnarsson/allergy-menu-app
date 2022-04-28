

$(".btn2").click(function(){
	 var m=new Object();
      m.id = this.id;
      console.log(m.id)
     post("https://www.doc.gold.ac.uk/usr/391/editmenu/",JSON.stringify(m))

        window.location.href = 'https://www.doc.gold.ac.uk/usr/391/editmenu/';
});



