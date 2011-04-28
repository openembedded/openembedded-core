require opkg_svn.bb

DEPENDS = "curl"
PROVIDES += "opkg"

SRCREV = "596"

EXTRA_OECONF += "--disable-gpg"

DEFAULT_PREFERENCE = "-1"
