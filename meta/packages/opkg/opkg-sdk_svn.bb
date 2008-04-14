require opkg.inc

DEPENDS = "curl-sdk"
PR = "r2"

inherit sdk

EXTRA_OECONF += "--with-opkglibdir=${target_libdir} --disable-gpg"
