
import subprocess
import bb.process

def detect_revision(d):
    path = get_scmbasepath(d)
    return get_metadata_git_revision(path, d)

def detect_branch(d):
    path = get_scmbasepath(d)
    return get_metadata_git_branch(path, d)

def get_scmbasepath(d):
    return os.path.join(d.getVar('COREBASE'), 'meta')

def get_metadata_svn_revision(path, d):
    # This only works with older subversion. For newer versions 
    # this function will need to be fixed by someone interested
    revision = "<unknown>"
    try:
        with open("%s/.svn/entries" % path) as f:
            revision = f.readlines()[3].strip()
    except (IOError, IndexError):
        pass
    return revision

def get_metadata_git_branch(path, d):
    try:
        rev, _ = bb.process.run('git rev-parse --abbrev-ref HEAD', cwd=path)
    except bb.process.ExecutionError:
        rev = '<unknown>'
    return rev.strip()

def get_metadata_git_revision(path, d):
    try:
        rev, _ = bb.process.run('git rev-parse HEAD', cwd=path)
    except bb.process.ExecutionError:
        rev = '<unknown>'
    return rev.strip()
         
