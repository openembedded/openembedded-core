require pseudo.inc

PR = "r5"

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
    file://0001-pseudo_has_unload-add-function.patch \
    file://shutdownping.patch \
    file://pseudo-1.5.1-install-directory-mode.patch \
    file://pseudo-fchmodat-permissions.patch \
"

SRC_URI_append_class-nativesdk = " file://symver.patch"

SRC_URI_append_class-native = " file://symver.patch"

SRC_URI[md5sum] = "5ec67c7bff5fe68c56de500859c19172"
SRC_URI[sha256sum] = "3b896f592f4d568569bd02323fad2d6b8c398e16ca36ee5a8947d2ff6c1d3d52"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async"
