times = [1, 100, 2000, 3000, 4000, 5000]
for( let i=0; i < times.length; i++ ){
	setTimeout( function(){window.scrollBy(0,5000);}, times[i] )
}
