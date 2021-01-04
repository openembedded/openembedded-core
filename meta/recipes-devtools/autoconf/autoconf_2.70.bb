require autoconf.inc

LICENSE = "GPLv2 & GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=cc3f3a7596cb558bbd9eb7fbaa3ef16c \
		    file://COPYINGv3;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "${GNU_MIRROR}/autoconf/${BP}.tar.gz \
           file://from-master.patch \
           file://program_prefix.patch \
           file://autoreconf-exclude.patch \
           file://remove-usr-local-lib-from-m4.patch \
           file://preferbash.patch \
           file://autotest-automake-result-format.patch \
           file://man-host-perl.patch \
           "

SRC_URI[sha256sum] = "f05f410fda74323ada4bdc4610db37f8dbd556602ba65bc843edb4d4d4a1b2b7"

SRC_URI_append_class-native = " file://no-man.patch"

EXTRA_OECONF += "ac_cv_path_M4=m4 ac_cv_prog_TEST_EMACS=no"

BBCLASSEXTEND = "native nativesdk"
