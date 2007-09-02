EXTRA_OEMAKE = ""
DEPENDS  += "${@["python-native python", ""][(bb.data.getVar('PACKAGES', d, 1) == '')]}"
RDEPENDS += "python-core"

def python_dir(d):
	import os, bb
	staging_incdir = bb.data.getVar( "STAGING_INCDIR", d, 1 )
	if os.path.exists( "%s/python2.5" % staging_incdir ): return "python2.5"
	if os.path.exists( "%s/python2.4" % staging_incdir ): return "python2.4"
	if os.path.exists( "%s/python2.3" % staging_incdir ): return "python2.3"
	raise "No Python in STAGING_INCDIR. Forgot to build python-native ?"

PYTHON_DIR = "${@python_dir(d)}"
FILES_${PN} = "${bindir}/* ${libdir}/* ${libdir}/${PYTHON_DIR}/*"
FILES_${PN}-dbg = "${libdir}/${PYTHON_DIR}/site-packages/.debug \
                   ${libdir}/${PYTHON_DIR}/site-packages/*/.debug \
                   ${libdir}/${PYTHON_DIR}/site-packages/*/*/.debug"

