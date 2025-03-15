SUMMARY = "scdoc is a simple man page generator for POSIX systems written in C99."
HOMEPAGE = "https://github.com/ddevault/scdoc"
BUGTRACKER = "https://github.com/ddevault/scdoc/issues"
SECTION = "base/doc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=31752b4a8edd3fb9ddba1cb90fada74d"

SRC_URI = " \
	git://git.sr.ht/~sircmpwn/scdoc;protocol=https;branch=master \
	file://0001-Makefile-dont-add-static-to-LDFLAGS.patch \
"

DEPENDS = "scdoc-native"

S = "${WORKDIR}/git"
PV = "1.11.2"
SRCREV = "afeda241f3f9b2c27e461f32d9c2a704ab82ef61"

EXTRA_OEMAKE:class-native = "PREFIX=${prefix}"
EXTRA_OEMAKE:class-target = "PREFIX=${prefix} HOST_SCDOC=${STAGING_BINDIR_NATIVE}/scdoc"

inherit autotools-brokensep
	
BBCLASSEXTEND = "native"

