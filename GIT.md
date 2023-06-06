### Commit rules

    git commit -m "type(scope) Reference message"

    Example: git commit -m 'feat(login) Create UI login’.

    Use: git commit --amend => used when we want to modify the last commit.
        or: git commit --amend -m "type(scope) Reference message"
    Afer: git push -f 

### Type list

    'add', Add new.

    'modify', // A modify.

    'docs', // Documentation only changes.

    'feat', // A new feature.

    'fix', // A bug fix.

    'perf', // A code change that improves performance.

    'refactor', // A code change that neither fixes a bug nor adds a feature.

    'revert', revert a ~HEAD commit.

    'test' // Adding missing tests or correcting existing tests.

    'style', // Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc).


### Git Flow

    Do not commit to `master`/ `develop`

    branch `master` or `main` for release version, after release will tag release',
    branch 'develop' for delivery a build to QA/QC

    When creating a new project, the base code will be pushed to `master`
    Member needs to checkout to branch `develop`, then checkout to new branch `[feature name]` to do
    After completing the task, member makes a pull request to branch `develop` and assigns leader to review and merge source

    The merge from develop to master will be merged by leader after version pushlished store

    **Steps to develop a new app**
     1. Create a new project on the store
     2. Push base source to branch `master`
     3. Checkout new branch developer from master
     4. Checkout new branch `[feature name]` from developer
     5. Create merge request from feature branch into developer branch
     6. Leader will review source and approve when all task is clean

    Note: Leader should be check squash commits


    **Steps to update exist app**
     If the project does not have a developer branch, report it to the leader before starting work