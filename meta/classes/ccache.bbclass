CCACHE = "${@bb.which(d.getVar('PATH', True), 'ccache') and 'ccache '}"
export CCACHE_DIR = "${TMPDIR}/ccache/${MULTIMACH_HOST_SYS}/${PN}"

do_configure[dirs] =+ "${CCACHE_DIR}"
do_kernel_configme[dirs] =+ "${CCACHE_DIR}"
