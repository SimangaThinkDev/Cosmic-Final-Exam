# Train question

- This will be needed to answer questions for the following functions:
    1. tagIn()
    2. transfer()
    3. tagOut()


This is just a mock train system, if you wish to build a more rigorous program, 
you are more than welcome, But this is to just simulate how a train system can
but it is not exactly correct


## Explanation

```
The Gautrain service has a tarrif service that they put in place
when a person tags in, they immediately deduct R12 from their bus
card, It only takes the actual full amount when the user commuter gets
off. Our functions will take in three inputs, origin ,and balance.

They are in an actual sense called in order, do not worry too much about
transfer, just deduct R2 from the balance when that is called

we have only three destinations possible:
    1. Rosebank  - R20 more
    2. Sandton   - R22 more
    3. Bryanston - R27 more

    - If the specified destination is not in the above three, we just 
      deduct R12
```

## Use the unit tests to guide you here...
