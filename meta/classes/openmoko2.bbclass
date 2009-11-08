inherit autotools pkgconfig

HOMEPAGE = "http://www.openmoko.org"
OPENMOKO_RELEASE ?= "OM-2007.2"
OPENMOKO_MIRROR ?= "svn://svn.openmoko.org/trunk"

def openmoko_two_get_license(d):
    openmoko, section = bb.data.getVar('SECTION', d, 1).split("/")
    return "LGPL GPL".split()[section != "libs"]

def openmoko_two_get_subdir(d):
    openmoko, section = bb.data.getVar('SECTION', d, 1).split("/")
    if section == 'base': return ""
    elif section == 'libs': return "libraries"
    elif section in 'apps tools pim'.split(): return "applications"
    elif section == "panel-plugin": return "panel-plugins"
    elif section == "inputmethods": return "inputmethods"
    elif section == "daemons": return "daemons"
    elif section == "misc": return "misc"
    else: return section

LICENSE = "${@openmoko_two_get_license(d)}"
SUBDIR = "${@openmoko_two_get_subdir(d)}"

SRC_URI := "${OPENMOKO_MIRROR}/src/target/${OPENMOKO_RELEASE}/${SUBDIR};module=${PN};proto=http"
S = "${WORKDIR}/${PN}"

FILES_${PN} += "${datadir}/icons"

SVNREV = "r${SRCREV}"
#SVNREV = "${SRCDATE}"
