import React from "react";
import { Dialog, Button, DialogActions, CircularProgress } from "@material-ui/core";
const ConfirmationDialog = ({
  open,
  onConfirmDialogClose,
  text,
  title,
  agree,
  cancel,
  onYesClick, loading
}) => {
  return (
    <Dialog
      maxWidth="xs"
      fullWidth={true}
      open={open}
      onClose={onConfirmDialogClose}
    >
      <div className="pt-24 px-20 pb-8">
        <h4 className="capitalize">{title}</h4>
        <p>{text}</p>
        <DialogActions>
          <div className="flex flex-space-between flex-middle">
            <Button
              variant="contained"
              color="secondary"
              className="mr-36"
              onClick={onConfirmDialogClose}
            >
              {cancel}
            </Button>

            <div style={{ position: "relative" }}>
              {loading ? (
                <CircularProgress
                  size={24}
                  style={{ 
                  }}
                />
              ) : <Button
                variant="contained"
                color="primary"
                onClick={onYesClick}
                disabled={loading}
              >
                  {agree}
                </Button>}
            </div>


          </div>
        </DialogActions>

      </div>
    </Dialog>
  );
};

export default ConfirmationDialog;
