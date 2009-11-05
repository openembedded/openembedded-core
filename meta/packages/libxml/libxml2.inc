DESCRIPTION = "GNOME XML Parser library"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "MIT"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxml2-${PV}.tar.gz \
           file://libxml-64bit.patch;patch=1 \
	   file://libxml-nanohttp.patch;patch=1"

DEPENDS_virtclass-native = "python-native"

inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--without-python --without-debug --without-legacy --without-catalog --without-docbook --with-c14n"
EXTRA_OECONF_virtclass-native = "--with-python=${STAGING_DIR_NATIVE}/${prefix} --without-debug --without-legacy --with-catalog --without-docbook --with-c14n"

export LDFLAGS += "-ldl"

python populate_packages_prepend () {
	# autonamer would call this libxml2-2, but we don't want that
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_libxml2', 'libxml2', d)
}

PACKAGES = "${PN}-dbg ${PN}-dev ${PN}-utils ${PN} ${PN}-doc ${PN}-locale"

FILES_${PN}-dev += "${bindir}/*-config"
FILES_${PN}-utils += "${bindir}/*"

BBCLASSEXTEND = "native"
