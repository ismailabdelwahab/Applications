# Dowload mp3 files from youtube links.
This little folder is used to download audio from a youtube video by providing it's link.

## Requirement
>Python library : **pytube** (used to download mp4 files from youtube)

>Command line : **ffmpeg** (used to convert mp4 to mp3)
## Inputs : How to select links to download.
In **musics_links.csv** you need the provide data as such:
> <youtube_link>**,**<mp3_filename_you_want>

One line per link and do not forget the **.mp3** extension on the name you want.

## Outputs : The musics/ folder
Your audio files will be stored in the folder **musics/**.

If this folder does not exists, it will be created.

All mp4 files (video) will be deleted after we have extracted the mp3.
