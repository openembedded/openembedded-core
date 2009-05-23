require opkg.inc

DEPENDS = "curl-sdk"
PR = "r3"

inherit sdk

EXTRA_OECONF += "--with-opkglibdir=${target_libdir} --disable-gpg"
