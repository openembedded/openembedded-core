CCACHE = "${@bb.utils.which(d.getVar('PATH'), 'ccache') and 'ccache '}"
export CCACHE_DIR ?= "${TMPDIR}/ccache/${MULTIMACH_HOST_SYS}/${PN}"
CCACHE_DISABLE[unexport] = "1"

DEPENDS_append_class-target = " ccache-native"
DEPENDS[vardepvalueexclude] = " ccache-native"

do_configure[dirs] =+ "${CCACHE_DIR}"
do_kernel_configme[dirs] =+ "${CCACHE_DIR}"
