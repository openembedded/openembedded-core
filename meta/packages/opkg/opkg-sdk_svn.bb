require opkg.inc

DEPENDS = "curl-sdk"

inherit sdk

EXTRA_OECONF += "--with-opkglibdir=${target_libdir}/opkg -disable-gpg"
