require opkg_${PV}.bb

DEPENDS = "curl"
PROVIDES += "opkg"

FILESEXTRAPATHS_prepend := "${THISDIR}/opkg-${PV}:"

EXTRA_OECONF += "--disable-gpg"

DEFAULT_PREFERENCE = "-1"
