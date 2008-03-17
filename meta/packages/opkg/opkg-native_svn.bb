require opkg.inc

DEPENDS = "curl-native"

target_libdir := "${libdir}"

inherit native

EXTRA_OECONF += "--with-opkglibdir=${target_libdir}/opkg -disable-gpg"
