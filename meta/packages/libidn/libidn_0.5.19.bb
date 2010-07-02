DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
# change to GPLv3 for >1.0 version
LICENSE     = "LGPLv2.1+ & GPLv2+"
PR          = "r1"

inherit pkgconfig autotools gettext

SRC_URI = "http://josefsson.org/libidn/releases/${P}.tar.gz"

EXTRA_OECONF = " --disable-tld"

do_configure_prepend () {
	autoreconf -f -i -s
}
