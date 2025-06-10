#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

# This class captures the host's dmesg output at the start and completion of a build.
# It stores these outputs in a variable and upon build completion, computes a diff between
# the two logs to detect any changes that occurred during the build process. It prints the diff
# using bb.warn(). The user needs to have privileges to run dmesg.

def append_log(msg, d):
    """
    Appends a log message to variable '_dmesg_log'
    """
    current_log = d.getVar('_dmesg_log') or ''
    current_log += msg + '\n'
    d.setVar('_dmesg_log', current_log)

def capture_dmesg():
    """
    Returns the current output of dmesg command
    """
    import subprocess

    try:
        result = subprocess.run(['dmesg'], capture_output=True, text=True, check=False)
        if result.returncode != 0:
            return f"Error running dmesg: {result.stderr.strip()}"
        return result.stdout
    except Exception as e:
        return f"Exception running dmesg: {str(e)}"


addhandler hostdmesglogger_eventhandler
python hostdmesglogger_eventhandler() {
    import difflib

    start_marker = "=== BuildStarted dmesg ==="
    end_marker = "=== BuildCompleted dmesg ==="
    diff_marker = "=== dmesg diff ==="

    # execute dmesg when BuildStarted event is fired
    if isinstance(e, bb.event.BuildStarted):
        dmesg_output = capture_dmesg()
        if dmesg_output.startswith("Error running dmesg:") or dmesg_output.startswith("Exception running dmesg:"):
            bb.warn(dmesg_output)
        else:
            append_log(start_marker, d)
            append_log(dmesg_output, d)

    # execute dmesg when BuildCompleted event is fired
    if isinstance(e, bb.event.BuildCompleted):
        dmesg_output = capture_dmesg()
        if dmesg_output.startswith("Error running dmesg:") or dmesg_output.startswith("Exception running dmesg:"):
            bb.warn(dmesg_output)
        else:
            append_log(end_marker, d)
            append_log(dmesg_output, d)

        content = d.getVar('_dmesg_log') or ''
        if start_marker in content and end_marker in content:
            start_dmesg = content.split(start_marker)[1].split(end_marker)[0]
            end_dmesg = content.split(end_marker)[1]

            start_lines = start_dmesg.strip().splitlines()
            end_lines = end_dmesg.strip().splitlines()

            # generating diff between BuildStarted and BuildCompleted dmesg outputs
            diff = list(difflib.unified_diff(
                start_lines, end_lines,
                fromfile='dmesg_start',
                tofile='dmesg_end',
                lineterm=''
            ))

            append_log(diff_marker, d)
            if diff:
                for line in diff:
                    bb.warn(line)
            else:
                bb.warn("No differences in dmesg output.")
        else:
            bb.warn("Could not find both dmesg sections for diff.")
}
hostdmesglogger_eventhandler[eventmask] = "bb.event.BuildStarted bb.event.BuildCompleted"
