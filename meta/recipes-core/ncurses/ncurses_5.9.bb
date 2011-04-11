require ncurses.inc

PR = "${INC_PR}.1"

SRC_URI += "file://tic-hang.patch \
            file://config.cache \
"

SRC_URI[md5sum] = "8cb9c412e5f2d96bc6f459aa8c6282a1"
SRC_URI[sha256sum] = "9046298fb440324c9d4135ecea7879ffed8546dd1b58e59430ea07a4633f563b"
