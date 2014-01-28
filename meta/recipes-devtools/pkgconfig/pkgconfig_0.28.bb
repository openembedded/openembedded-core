require pkgconfig.inc

SRC_URI += " \
            file://pkg-config-native.in \
            file://fix-glib-configure-libtool-usage.patch \
            file://obsolete_automake_macros.patch \
           "

SRC_URI[md5sum] = "aa3c86e67551adc3ac865160e34a2a0d"
SRC_URI[sha256sum] = "6b6eb31c6ec4421174578652c7e141fdaae2dabad1021f420d8713206ac1f845"
