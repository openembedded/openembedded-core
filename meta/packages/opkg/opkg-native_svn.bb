require opkg.inc

DEPENDS = "curl-native"
PROVIDES += "virtual/update-alternatives-native"
RPROVIDES_${PN} += "update-alternatives-native"
PR = "r2"

target_libdir := "${libdir}"

inherit native

EXTRA_OECONF += "--with-opkglibdir=${target_libdir} --disable-gpg"
