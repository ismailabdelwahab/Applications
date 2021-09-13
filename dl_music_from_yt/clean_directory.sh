#!/bin/sh

# Delete mp4 files
rm *.mp4

# Move mp3 files to a folder called "musics".
if ! [ -d musics ]; then
  mkdir musics
fi

mv *.mp3 musics/
