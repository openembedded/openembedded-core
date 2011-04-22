# The following logging mechanisms are to be used in bash functions of recipes.
# They are intended to map one to one in intention and output format with the
# python recipe logging functions of a similar naming convention: bb.plain(),
# bb.note(), etc.
#
# For the time being, all of these print only to the task logs. Future
# enhancements may integrate these calls with the bitbake logging
# infrastructure, allowing for printing to the console as appropriate. The
# interface and intention statements reflect that future goal. Once it is
# in place, no changes will be necessary to recipes using these logging
# mechanisms.

# Print the output exactly as it is passed in. Typically used for output of
# tasks that should be seen on the console. Use sparingly.
# Output: logs console
# NOTE: console output is not currently implemented.
bbplain() {
	echo "$*"
}

# Notify the user of a noteworthy condition. 
# Output: logs console
# NOTE: console output is not currently implemented.
bbnote() {
	echo "NOTE: $*"
}

# Print a warning to the log. Warnings are non-fatal, and do not
# indicate a build failure.
# Output: logs
bbwarn() {
	echo "WARNING: $*"
}

# Print an error to the log. Errors are non-fatal in that the build can
# continue, but they do indicate a build failure.
# Output: logs
bberror() {
	echo "ERROR: $*"
}

# Print a fatal error to the log. Fatal errors indicate build failure
# and halt the build, exiting with an error code.
# Output: logs
bbfatal() {
	echo "ERROR: $*"
	exit 1
}

# Print debug messages. These are appropriate for progress checkpoint
# messages to the logs. Depending on the debug log level, they may also
# go to the console.
# Output: logs console
# Usage: bbdebug 1 "first level debug message"
#        bbdebug 2 "second level debug message"
# NOTE: console output is not currently implemented.
bbdebug() {
	USAGE='Usage: bbdebug [123] "message"'
	if [ $# -lt 2 ]; then
		bbfatal "$USAGE"
	fi
	
	# Strip off the debug level and ensure it is an integer
	DBGLVL=$1; shift
	if ! [[ "$DBGLVL" =~ ^[0-9]+ ]]; then
		bbfatal "$USAGE"
	fi

	# All debug output is printed to the logs
	echo "DEBUG: $*"
}

