SUMMARY = "Software Construction tool (make/autotools replacement)"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=46ddf66004e5be5566367cb525a66fc6"
SRCNAME = "scons"

SRC_URI = "https://files.pythonhosted.org/packages/source/s/${SRCNAME}/${SRCNAME}-${PV}.tar.gz \
           file://SConscript-Support-python2-print-statements.patch"

SRC_URI[md5sum] = "7ca558edaaa1942fe38f3105ca2400fb"
SRC_URI[sha256sum] = "aa5afb33c2bbd33c311e47e912412195739e9ffb2e933534a31f85fba8f3470e"

UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/SCons/"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools

RDEPENDS_${PN} = "\
  python-fcntl \
  python-io \
  python-json \
  python-subprocess \
  python-shell \
  python-pprint \
  python-importlib \
  "
