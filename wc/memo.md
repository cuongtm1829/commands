## Create file with null terminated filenames
1. find /Users/trancuong/Desktop/test.html -print0  > files.txt
## Command in out/production/wc
- find /Users/trancuong/Desktop/test.html | java com.gm.Main
- java com.gm.Main --version
### Ubuntu:
find . -name "*" -print0 | xargs -0 wc
