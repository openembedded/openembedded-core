# This file is for getting archiving packages with original
# sources(archive ${S} after unpack stage), patches, logs(archive 'temp'
# after package_write_rpm), dump data and creating diff file(get all
# environment variables and functions in building and mapping all
# content in ${S} including patches to xxx.diff.gz. All archived packages
# will be deployed in ${DEPLOY_DIR}/sources

inherit archiver

# Get original sources archiving package with patches
addtask do_archive_original_sources_patches after do_unpack

# Get archiving package with temp(logs) and scripts(.bb and inc files)
addtask do_archive_scripts_logs

# Get dump date and create diff file 
addtask do_dumpdata_create_diff_gz

python () {
    pn = d.getVar('PN', True)
    packaging = d.getVar('IMAGE_PKGTYPE', True)

    if tar_filter(d):
        return

    d.appendVarFlag('do_dumpdata_create_diff_gz', 'depends', ' %s:do_package_write_' %pn + packaging)
    build_deps = ' %s:do_dumpdata_create_diff_gz' %pn

    if d.getVar('SOURCE_ARCHIVE_LOG_WITH_SCRIPTS', True) == 'logs_with_scripts':
        d.appendVarFlag('do_archive_scripts_logs', 'depends', ' %s:do_package_write_' %pn + packaging)
        build_deps += ' %s:do_archive_scripts_logs' %pn

    if not not_tarball(d):
        d.appendVarFlag('do_patch', 'depends', ' %s:do_archive_original_sources_patches' %pn)
        build_deps += ' %s:do_archive_original_sources_patches' %pn

    if bb.data.inherits_class('image', d):
        d.appendVarFlag('do_rootfs', 'depends', build_deps)
    else:
        d.appendVarFlag('do_build', 'depends', build_deps)
}

ARCHIVE_SSTATE_OUTDIR = "${DEPLOY_DIR}/sources/"
ARCHIVE_SSTATE_SCRIPTS_LOGS_INDIR = "${WORKDIR}/script-logs/"
ARCHIVE_SSTATE_DIFFGZ_ENVDATA_INDIR = "${WORKDIR}/diffgz-envdata/"

SSTATETASKS += "do_archive_scripts_logs"
do_archive_scripts_logs[sstate-name] = "archive_scripts_logs"
do_archive_scripts_logs[sstate-inputdirs] = "${ARCHIVE_SSTATE_SCRIPTS_LOGS_INDIR}"
do_archive_scripts_logs[sstate-outputdirs] = "${ARCHIVE_SSTATE_OUTDIR}"

python do_archive_scripts_logs_setscene () {
    sstate_setscene(d)
}

addtask do_archive_scripts_logs_setscene

SSTATETASKS += "do_dumpdata_create_diff_gz"
do_dumpdata_create_diff_gz[sstate-name] = "dumpdata_create_diff_gz"
do_dumpdata_create_diff_gz[sstate-inputdirs] = "${ARCHIVE_SSTATE_DIFFGZ_ENVDATA_INDIR}"
do_dumpdata_create_diff_gz[sstate-outputdirs] = "${ARCHIVE_SSTATE_OUTDIR}"

python do_dumpdata_create_diff_gz_setscene () {
    sstate_setscene(d)
}

addtask do_dumpdata_create_diff_gz_setscene
