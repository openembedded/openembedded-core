# This file is for getting archiving packages with configured sources(archive 's' after configure stage),logs(archive 'temp' after package_write_rpm),dump data 
# and creating diff file(get all environment variables and functions in building and mapping all content in 's' including patches to  xxx.diff.gz.
# All archived packages will be deployed in ${DEPLOY_DIR}/sources

inherit archiver

# Get archiving package with configured sources including patches
do_configure[postfuncs] += "do_archive_configured_sources "

# Get archiving package with temp(logs) and scripts(.bb and inc files)
do_package_write_rpm[prefuncs] += "do_archive_scripts_logs "

# Get dump date and create diff file 
do_package_write_rpm[postfuncs] += "do_dumpdata_create_diff_gz "
