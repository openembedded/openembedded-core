require opkg.inc

DEPENDS = "curl-native"
PR = "r1"

target_libdir := "${libdir}"

inherit native

EXTRA_OECONF += "--with-opkglibdir=${target_libdir} -disable-gpg"
