DESCRIPTION = "Gives a fake root environment"
HOMEPAGE = "http://fakeroot.alioth.debian.org/"
SECTION = "base"
LICENSE = "GPL"
# fakeroot needs getopt which is provided by the util-linux package
RDEPENDS = "util-linux"
RDEPENDS_virtclass-native = "util-linux-native"
PR = "r2"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakeroot/fakeroot_${PV}.tar.gz \
           file://configure-libtool.patch;patch=1"
	    
inherit autotools_stage

do_install_append() {
	install -d ${D}${STAGING_INCDIR}/fakeroot/
	install -m 644 *.h ${D}${STAGING_INCDIR}/fakeroot
}

# Compatability for the rare systems not using or having SYSV
python () {
    if bb.data.inherits_class("native", d) and bb.data.getVar('HOST_NONSYSV', d, True) and bb.data.getVar('HOST_NONSYSV', d, True) != '0':
        bb.data.setVar('EXTRA_OECONF', ' --with-ipc=tcp ', d)
}

BBCLASSEXTEND = "native"