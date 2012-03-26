# This file is for getting archiving packages with original sources(archive 's' after unpack stage),patches,logs(archive 'temp' after package_write_rpm),dump data and 
# creating diff file(get all environment variables and functions in building and mapping all content in 's' including patches to  xxx.diff.gz.
# All archived packages will be deployed in ${DEPLOY_DIR}/sources

inherit archiver

# Get original sources archiving package with patches
do_unpack[postfuncs] += "do_archive_original_sources_patches "

# Get archiving package with temp(logs) and scripts(.bb and inc files)
do_package_write_rpm[prefuncs] += "do_archive_scripts_logs "

# Get dump date and create diff file 
do_package_write_rpm[postfuncs] += "do_dumpdata_create_diff_gz "
