var map;
var idInfoBoxAberto;
var infoBox = [];
var markers = [];

function initialize() {	
	var centro = {lat:-3.7281295, lng:-38.4954897};

	var latlng = new google.maps.LatLng(centro);
	
    var options = {
        zoom: 17,
	center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("mapa"), options);
}

initialize();

function abrirInfoBox(id, marker) {
	if (typeof(idInfoBoxAberto) == 'number' && typeof(infoBox[idInfoBoxAberto]) == 'object') {
		infoBox[idInfoBoxAberto].close();
	}

	infoBox[id].open(map, marker);
	idInfoBoxAberto = id;
}

function carregarPontos() {
	
	$.getJSON('/dist/js/mapsStores.json', function(pontos) {
		
		var latlngbounds = new google.maps.LatLngBounds();
		
		$.each(pontos, function(index, ponto) {
			var pontoDegree = {lat: ponto.latitude, lng: ponto.longitude};
			var marker = new google.maps.Marker({
				position: new google.maps.LatLng(pontoDegree),
				title: ponto.name,
				label: ponto.name, 
				icon: '/dist/img/marcador.png'
			});
			
			var myOptions = {
				content: "<p>" + ponto.id + "</p>",
				pixelOffset: new google.maps.Size(-50, 0)
        	};

			infoBox[ponto.id] = new InfoBox(myOptions);
			infoBox[ponto.id].marker = marker;
			
			infoBox[ponto.id].listener = google.maps.event.addListener(marker, 'click', function (e) {
				abrirInfoBox(ponto.id, marker);
			});
			
			markers.push(marker);
			
			latlngbounds.extend(marker.position);
			
		});
		
		var markerCluster = new MarkerClusterer(map, markers, {imagePath: '/dist/img/m'});
		
		map.fitBounds(latlngbounds);
		
	});
	
}
carregarPontos();