function tema(link,tema){
var todos = link.parentNode.parentNode.getElementsByTagName('a');
for (var i=0;i<todos.length;i++){todos[i].className='';}
	link.className='at';
	document.getElementById('geral').className=tema;
}
function contrai(){var corpo=document.getElementById("corpo");if(corpo.className==""){corpo.className="sem-menu";}else{corpo.className="";}}
