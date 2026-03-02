function slide(a) {
	clearInterval(e);
	if (a) {
		i = i - 2;
	}
	ani();
	e = setInterval('ani()', 8300)
}
function slideApp(a) {
	clearInterval(e1);
	if (a) {
		i1 = i1 - 2;
	}
	aniApp();
	e = setInterval('aniApp()', 8300)
}
function ani() {
	i++;
	if (i == t) {
		i = 0
	}
	if (i == -1) {
		i = t - 1
	}
	for ( var b = 0; b < t; b++) {
		l[b].className = "";
	}
	l[i].className = 'move';
}
function aniApp() {
	i1++;
	if (i1 == t1) {
		i1 = 0;
	}
	if (i1 == -1) {
		i1 = t1 - 1;
	}
	for ( var b = 0; b < t1; b++) {
		l1[b].className = "";
	}
	l1[i1].className = 'move';
}
if (document.getElementById('slide')) {
	var l = document.getElementById('slide').getElementsByTagName('li');
	var i = -1;
	var t = l.length;
	var e;
	slide();
}
if (document.getElementById('slideApp')) {
	var l1 = document.getElementById('slideApp').getElementsByTagName('li');
	var i1 = -1;
	var t1 = l1.length;
	var e1;
	slideApp();
}

function usuario() {
	var usuario = document.getElementById('opUsuario');
	if (usuario.className == 'opcoes-usuario') {
		usuario.className = 'opcoes-usuario none';
	} else {
		usuario.className = 'opcoes-usuario';
	}
}



