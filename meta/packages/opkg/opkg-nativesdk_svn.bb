require opkg.inc

DEPENDS = "curl-nativesdk"
PR = "r3"

inherit nativesdk

EXTRA_OECONF += "--with-opkglibdir=${target_libdir} --disable-gpg"
