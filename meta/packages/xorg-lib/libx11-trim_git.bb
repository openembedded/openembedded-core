require libx11.inc
PV = "1.1.99.1+gitr${SRCREV}"
PR = "r1"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libX11;protocol=git"
S = "${WORKDIR}/git"

SRC_URI += "file://x11_disable_makekeys.patch;patch=1 \
            file://include_fix.patch;patch=1"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11-git"

EXTRA_OECONF += "--disable-xcms --with-xcb"
CFLAGS += "-D_GNU_SOURCE"

FILESPATH = "${FILE_DIRNAME}/libx11-git"
