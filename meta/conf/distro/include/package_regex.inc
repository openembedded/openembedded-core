# NOTE: THIS FILE IS DEPRECATED. Please add new entries to the recipes themselves.
#
# package_regex.inc - This file contains data that tracks
# upstream project associated with a given recipe. This list is needed
# for recipes that version information can not be automagically discovered.
# As we automate this checking, this file will slowly be reduced.
#"
# This data is used by the package reporting system (packages.yoctoproject.org)
#"
# The format is as a bitbake variable override for each recipe
#"
#       UPSTREAM_CHECK_URI_pn-<recipe name> = "recipe_url"
#	 - This is the url used by the package checking system to
#	   get the latest version of the package
#       UPSTREAM_CHECK_REGEX_pn-<recipe name> = "package_regex"
#	 - This is the regex the package checking system uses to
#	   parse the page found at UPSTREAM_CHECK_URI_pn-<recipe name>
#	UPSTREAM_CHECK_GITTAGREGEX_pn-<recipe name> = "git_tag_regex"
#	 - When source code is fetched from git, git tags are used to
#	   determine the upstream release version. This regex can be used
#	   to filter only relevant tags.
#

COMMON_REGEX = "(?P<pver>(\d+[\.\-_]*)+)"

# Generic regex don't match
REGEX_URI_pn-wireless-tools = "http://www.hpl.hp.com/personal/Jean_Tourrilhes/Linux/Tools.html"
REGEX_pn-wireless-tools = "wireless_tools\.(?P<pver>(\d+)(\..*|))\.tar\.gz"

REGEX_URI_pn-sqlite3="http://www.sqlite.org/"
REGEX_pn-sqlite3="releaselog/${COMMON_REGEX}.html"

REGEX_URI_pn-dhcp = "ftp://ftp.isc.org/isc/dhcp/"
REGEX_pn-dhcp="(?P<pver>\d+\.\d+\.(\d+?))/"

REGEX_pn-foomatic-filters = "foomatic-filters-(?P<pver>((\d|\d\d)\.*)+)\.tar\.gz"
REGEX_pn-xdg-utils = "xdg-utils-(?P<pver>((\d+[\.\-_]*)+)((rc|alpha|beta)\d+)?)\.(tar\.gz|tgz)"
REGEX_pn-rpm="rpm-${COMMON_REGEX}-.*$"
REGEX_pn-libtheora = "libtheora-(?P<pver>\d+(\.\d)+)\.(tar\.gz|tgz)"

REGEX_pn-iputils="iputils-(?P<pver>s\d+).tar"
REGEX_pn-nettle = "nettle-(?P<pver>\d+(\.\d+)+)\.tar"

REGEX_pn-cairo = "cairo-(?P<pver>\d+(\.\d+)+)\.tar"
REGEX_pn-cmake = "cmake-(?P<pver>\d+(\.\d+)+)\.tar"
REGEX_pn-libsdl = "SDL-(?P<pver>\d+(\.\d+)+)\.tar"
REGEX_pn-libxslt = "libxslt-(?P<pver>\d+(\.\d+)+)\.tar"

REGEX_URI_pn-autogen = "http://ftp.gnu.org/gnu/autogen/"
REGEX_pn-autogen = "rel(?P<pver>\d+(\.\d+)+)/"

# Isn't possible to download with the default URI web server returns
# (403, 404, 550)
REGEX_URI_pn-dosfstools = "https://github.com/dosfstools/dosfstools/releases"
REGEX_pn-json-c = "json-c-(?P<pver>\d+(\.\d+)+).tar"
# json-c releases page is fetching the list of releases in some weird XML format
# from https://s3.amazonaws.com/json-c_releases and processes it with javascript :-/
#REGEX_URI_pn-json-c = "https://s3.amazonaws.com/json-c_releases/releases/index.html"
REGEX_URI_pn-chrpath = "http://alioth.debian.org/frs/?group_id=31052"

REGEX_URI_pn-powertop = "https://01.org/powertop/downloads"
REGEX_URI_pn-libsamplerate0 = "http://www.mega-nerd.com/SRC/download.html"
REGEX_URI_pn-libxkbcommon = "http://xkbcommon.org/"
REGEX_URI_pn-tslib = "https://github.com/kergoth/tslib/releases"
REGEX_URI_pn-waffle="http://www.waffle-gl.org/releases.html"
REGEX_URI_pn-lrzsz = "http://ohse.de/uwe/software/lrzsz.html"
REGEX_URI_pn-libacpi = "http://www.ngolde.de/libacpi.html"
REGEX_URI_pn-less  = "http://www.greenwoodsoftware.com/less/download.html"
REGEX_URI_pn-cups = "http://www.cups.org/software.php"
REGEX_pn-cups = "cups-(?P<pver>\d+\.\d+(\.\d+)?)-source.tar"
REGEX_URI_pn-bzip2 = "http://www.bzip.org/downloads.html"
REGEX_URI_pn-minicom = "https://alioth.debian.org/frs/?group_id=30018"
REGEX_URI_pn-sysstat = "http://sebastien.godard.pagesperso-orange.fr/download.html"
REGEX_URI_pn-libical = "https://github.com/libical/libical/releases/"
REGEX_URI_pn-libmpc = "http://www.multiprecision.org/index.php?prog=mpc&page=download"
REGEX_URI_pn-taglib = "http://github.com/taglib/taglib/releases/"
REGEX_URI_pn-libevent = "http://libevent.org/"
REGEX_URI_pn-db = "http://www.oracle.com/technetwork/products/berkeleydb/downloads/index-082944.html"
REGEX_URI_pn-python-smartpm="https://launchpad.net/smart/trunk/"
REGEX_URI_pn-lsof = "ftp://sunsite.ualberta.ca/pub/Mirror/lsof/"
REGEX_URI_pn-pbzip2 = "http://compression.ca/pbzip2/"
REGEX_URI_pn-quicky = "http://qt-apps.org/content/download.php?content=80325&id=1&tan=10417482"
REGEX_URI_pn-libwebp = "http://downloads.webmproject.org/releases/webp/index.html"

# The engine tries to use directory versioning but is a false-positive
REGEX_URI_pn-intltool = "https://launchpad.net/intltool/trunk/"
REGEX_URI_pn-libnfsidmap = "http://www.citi.umich.edu/projects/nfsv4/linux/libnfsidmap/"
REGEX_URI_pn-mpfr = "http://www.mpfr.org/mpfr-current/"
REGEX_URI_pn-nss = "https://developer.mozilla.org/en-US/docs/Mozilla/Projects/NSS/NSS_Releases"
REGEX_pn-nss = "NSS_(?P<pver>.+)_release_notes"
