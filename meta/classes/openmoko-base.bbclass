HOMEPAGE = "http://www.openmoko.org"
LICENSE ?= "GPL"
OPENMOKO_RELEASE ?= "OM-2007"
OPENMOKO_MIRROR ?= "svn://svn.openmoko.org/trunk"

def openmoko_base_get_subdir(d):
    import bb
    openmoko, section = bb.data.getVar('SECTION', d, 1).split("/")
    if section == 'base' or section == 'libs': return ""
    elif section in 'apps tools pim'.split(): return "applications"
    elif section == "panel-plugin": return "panel-plugins"
    elif section == "inputmethods": return "inputmethods"
    else: return section

SUBDIR = "${@openmoko_base_get_subdir(d)}"

SRC_URI := "${OPENMOKO_MIRROR}/src/target/${OPENMOKO_RELEASE}/${SUBDIR};module=${PN};proto=http"
S = "${WORKDIR}/${PN}"

FILES_${PN} += "${datadir}/icons"
