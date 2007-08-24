# ash isn't good enough for quilt at least...
RDEPENDS_${PN} += "patch diffstat bzip2 bash"

require quilt_${PV}.inc

SRC_URI += "file://aclocal.patch;patch=1"

inherit autotools gettext

require quilt-package.inc
