require opkg_svn.bb

DEPENDS = "curl"
PROVIDES += "opkg"

SRCREV = "${SRCREV_pn-opkg}"

EXTRA_OECONF += "--disable-gpg"

DEFAULT_PREFERENCE = "-1"
