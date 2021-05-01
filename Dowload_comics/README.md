# ReadComicOnline Downloader

This scipt is used to download all pages of a comic from [**_readcomiconline.li_** ](https://readcomiconline.li/).

## Proper usage
To use the script simply do :

	$ ./dl_comic_from_readcomiconline.py <link from readcomiconline.li>

## Data saved by the script
The script will save on your computer the **HTML page** that you gave in argument. File :

	comic_html_page.html

Then it will create the directory **comic/** in which each page will be saved as **pageXX.jpg** with XX beeing the number of the page of the comic book.

## Content of directory after execution
	dl_comic_from_readcomiconline.py  <-- Original script
	
	comic_html_page.html <--------------- HTML page saved
	
	comic/ <----------------------------- Directory containing your dowloaded comic
		page1.jpg    |                                 
		page2.jpg    |<------------------ Saved pages
		...          |                                 
		page302.jpg  |                               

