## Create file with null terminated filenames
1. find /Users/trancuong/Desktop/test.html -print0  > files.txt
## Command in out/production/wc
- find /Users/trancuong/Desktop/test.html | java WC
- java WC --version
### Ubuntu:
find . -name "*" -print0 | xargs -0 wc
####
//        System.out.println(Arrays.toString(args));
//        run(new String[] { "--words", "/Users/trancuong/Desktop/test.html", "--lines", "/Users/trancuong/Desktop/test.html" });
//        run(new String[] { "--help"});
//        run(new String[] { "--version"});
//        run(new String[] { "--files0-from", "/Users/trancuong/Desktop/files.txt"});
//        run(new String[] { "-lw", "/Users/trancuong/Desktop/test.html", "/Users/trancuong/Desktop/test1.html"});
    