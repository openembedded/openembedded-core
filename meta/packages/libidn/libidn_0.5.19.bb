LICENSE     = "LGPL"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
SECTION = "libs"
PRIORITY    = "optional"
PR          = "r0"

inherit pkgconfig autotools

SRC_URI = "http://josefsson.org/libidn/releases/${P}.tar.gz"

EXTRA_OECONF = " --disable-tld"

do_configure_prepend () {
	autoreconf -f -i -s
}

do_stage () {
	autotools_stage_all
}
