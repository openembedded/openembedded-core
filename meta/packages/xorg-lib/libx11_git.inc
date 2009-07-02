PV = "1.1.99.1+gitr${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libX11;protocol=git"
S = "${WORKDIR}/git"

SRC_URI += "file://x11_disable_makekeys.patch;patch=1 \
            file://include_fix.patch;patch=1"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11-git"
FILESPATH = "${FILE_DIRNAME}/libx11-git"
