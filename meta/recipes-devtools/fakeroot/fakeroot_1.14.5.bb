DESCRIPTION = "Provides a fake \"root environment\" by means of LD_PRELOAD and SYSV IPC or TCP trickery"
HOMEPAGE = "http://fakeroot.alioth.debian.org/"
SECTION = "base"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949"

# fakeroot needs getopt which is provided by the util-linux package
RDEPENDS = "util-linux"
RDEPENDS_virtclass-native = "util-linux-native"
PR = "r0"
PROVIDES += "virtual/fakeroot"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakeroot/fakeroot_${PV}.orig.tar.bz2 \
           file://absolutepaths.patch"

inherit autotools

do_configure_prepend() {
	# fakeroot's own bootstrap includes other autoreconf stuff we don't need here
	# so manually create the aux directory
	mkdir -p ${S}/build-aux
}

do_install_append() {
	install -d ${D}${includedir}/fakeroot/
	install -m 644 *.h ${D}${includedir}/fakeroot
}

# Compatability for the rare systems not using or having SYSV
python () {
    if bb.data.inherits_class("native", d) and bb.data.getVar('HOST_NONSYSV', d, True) and bb.data.getVar('HOST_NONSYSV', d, True) != '0':
        bb.data.setVar('EXTRA_OECONF', ' --with-ipc=tcp ', d)
}

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "659a1f3a36554abfc2a3eaad2fdc0604"
SRC_URI[sha256sum] = "b035c834944bf9482027f48c388de8492e96609825265ac03f05408d0b3aae68"
